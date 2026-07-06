import{Injectable}from'@angular/core';
import{HttpClient,HttpParams}from'@angular/common/http';
import{Observable}from'rxjs';
import{environment}from'../../../environments/environment';
import{EventCost,FinancialSummary}from'../models/financial.model';

@Injectable({providedIn:'root'})
export class FinancialReportService{
  private readonly url=environment.apiUrl;
  constructor(private http:HttpClient){}

  summary():Observable<FinancialSummary>{
    return this.http.get<FinancialSummary>(this.url+'/financial-reports/summary');
  }

  eventCosts(filters:{eventName?:string;startDate?:string;endDate?:string;exitTypeId?:string|number|null}):Observable<EventCost>{
    return this.http.get<EventCost>(this.url+'/financial-reports/event-costs',{params:this.params(filters)});
  }

  download(path:string,filters:Record<string,unknown>={}):void{
    this.http.get(this.url+path,{params:this.params(filters),responseType:'blob',observe:'response'}).subscribe({
      next:response=>{
        const blob=response.body;
        if(!blob)return;
        const filename=this.filenameFromHeader(response.headers.get('content-disposition'))||'relatorio.csv';
        const objectUrl=URL.createObjectURL(blob);
        const link=document.createElement('a');
        link.href=objectUrl;
        link.download=filename;
        document.body.appendChild(link);
        link.click();
        link.remove();
        URL.revokeObjectURL(objectUrl);
      },
      error:()=>alert('Não foi possível exportar o CSV. Confirme se você está logado e tente novamente.')
    });
  }

  private filenameFromHeader(header:string|null):string|null{
    if(!header)return null;
    const match=/filename="?([^"]+)"?/i.exec(header);
    return match?.[1]||null;
  }

  private params(filters:Record<string,unknown>):HttpParams{
    let p=new HttpParams();
    Object.entries(filters).forEach(([k,v])=>{
      if(v!==null&&v!==undefined&&String(v).trim()!=='')p=p.set(k,String(v));
    });
    return p;
  }
}
