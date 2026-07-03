export type UserRole='ADMIN'|'USER';export interface AuthUser{id:number;name:string;email:string;role:UserRole;}export interface LoginResponse{token:string;type:'Bearer';user:AuthUser;}
