import { configureStore, createSlice } from '@reduxjs/toolkit';
const auditSlice=createSlice({name:'audit',initialState:{loading:false,result:null,error:null},reducers:{start:s=>{s.loading=true;s.error=null;s.result=null},success:(s,a)=>{s.loading=false;s.result=a.payload},failure:(s,a)=>{s.loading=false;s.error=a.payload}}});
export const {start,success,failure}=auditSlice.actions;
export const store=configureStore({reducer:{audit:auditSlice.reducer}});
