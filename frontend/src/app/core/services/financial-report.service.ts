import{Injectable}from'@angular/core';
import{HttpClient,HttpParams}from'@angular/common/http';
import{Observable}from'rxjs';
import{environment}from'../../../environments/environment';
import{EventCost,FinancialSummary}from'../models/financial.model';
@Injectable({providedIn:'root'})
export class FinancialReportService{private readonly url=environment.apiUrl;constructor(private http:HttpClient){}
summary():Observable<FinancialSummary>{return this.http.get<FinancialSummary>(this.url+'/financial-reports/summary');}
eventCosts(filters:{eventName?:string;startDate?:string;endDate?:string;exitTypeId?:string|number|null}):Observable<EventCost>{return this.http.get<EventCost>(this.url+'/financial-reports/event-costs',{params:this.params(filters)});}
download(path:string,filters:Record<string,unknown>={}):void{const link=document.createElement('a');const q=this.params(filters).toString();link.href=this.url+path+(q?'?'+q:'');link.download='relatorio.csv';link.click();}
private params(filters:Record<string,unknown>):HttpParams{let p=new HttpParams();Object.entries(filters).forEach(([k,v])=>{if(v!==null&&v!==undefined&&String(v).trim()!=='')p=p.set(k,String(v));});return p;}
}
