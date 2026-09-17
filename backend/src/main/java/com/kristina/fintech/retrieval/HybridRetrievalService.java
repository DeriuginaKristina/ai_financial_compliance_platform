package com.kristina.fintech.retrieval;

import com.kristina.fintech.model.TextChunk;
import com.kristina.fintech.security.CorporateGuardrails;
import org.springframework.stereotype.Service;
import java.sql.*;
import java.util.*;

@Service
public class HybridRetrievalService {
  private final CorporateGuardrails guardrails; private final Connection db; private final Map<String,TextChunk> vectorIndex=new HashMap<>();
  public HybridRetrievalService(CorporateGuardrails guardrails){this.guardrails=guardrails;try{db=DriverManager.getConnection("jdbc:sqlite:file:compliance_rules?mode=memory&cache=shared");init();}catch(SQLException e){throw new IllegalStateException(e);}}
  private void init() throws SQLException{
    try(Statement s=db.createStatement()){s.execute("CREATE VIRTUAL TABLE IF NOT EXISTS rules_fts USING fts5(id UNINDEXED, content)");}
    seed("RULE-EXW","Incoterms EXW means the buyer bears costs and risks from the seller's premises.");
    seed("RULE-DDP","Incoterms DDP means the seller delivers goods cleared for import and bears delivery obligations.");
    seed("RULE-VIDA","ViDA is a phased EU VAT digitalisation framework. Cross-border B2B digital reporting changes are scheduled from 2030.");
    seed("RULE-ISO4217","Financial messages should carry an explicit ISO-style currency code and deterministic monetary precision.");
  }
  private void seed(String id,String text)throws SQLException{try(PreparedStatement p=db.prepareStatement("INSERT INTO rules_fts(id,content) VALUES(?,?)")){p.setString(1,id);p.setString(2,text);p.executeUpdate();} vectorIndex.put(id,new TextChunk(id,text,1));}
  public List<TextChunk> retrieve(String query){
    String match=String.join(" OR ", tokenize(query)); if(match.isBlank())return List.of(); List<TextChunk> out=new ArrayList<>();
    try(PreparedStatement p=db.prepareStatement("SELECT id,content,bm25(rules_fts) score FROM rules_fts WHERE rules_fts MATCH ? ORDER BY score LIMIT 10")){p.setString(1,match);try(ResultSet r=p.executeQuery()){while(r.next()){TextChunk c=new TextChunk(r.getString(1),r.getString(2),1.0/(1.0+Math.abs(r.getDouble(3))));if(guardrails.isSafe(c))out.add(guardrails.sanitize(c));}}}
    catch(SQLException e){return lexicalFallback(query);} return out.stream().sorted(Comparator.comparingDouble(TextChunk::score).reversed()).limit(5).toList();
  }
  private List<TextChunk> lexicalFallback(String q){Set<String> terms=tokenize(q);return vectorIndex.values().stream().map(c->new TextChunk(c.id(),c.content(),score(c.content(),terms))).filter(c->c.score()>0).filter(guardrails::isSafe).map(guardrails::sanitize).sorted(Comparator.comparingDouble(TextChunk::score).reversed()).limit(5).toList();}
  private double score(String text,Set<String> q){long h=tokenize(text).stream().filter(q::contains).count();return q.isEmpty()?0:(double)h/q.size();}
  private Set<String> tokenize(String s){Set<String> out=new HashSet<>();for(String x:s.toLowerCase(Locale.ROOT).split("[^a-z0-9]+")){if(x.length()>2)out.add(x);}return out;}
}
