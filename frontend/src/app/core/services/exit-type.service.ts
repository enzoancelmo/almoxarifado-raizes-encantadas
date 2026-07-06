import{Injectable}from'@angular/core';
import{HttpClient}from'@angular/common/http';
import{Observable}from'rxjs';
import{environment}from'../../../environments/environment';
import{ExitType,ExitTypePayload}from'../models/exit-type.model';
@Injectable({providedIn:'root'})
export class ExitTypeService{private readonly url=environment.apiUrl+'/exit-types';constructor(private http:HttpClient){}
list(activeOnly=false):Observable<ExitType[]>{return this.http.get<ExitType[]>(this.url+(activeOnly?'?activeOnly=true':''));}
get(id:number):Observable<ExitType>{return this.http.get<ExitType>(this.url+'/'+id);}
create(payload:ExitTypePayload):Observable<ExitType>{return this.http.post<ExitType>(this.url,payload);}
update(id:number,payload:ExitTypePayload):Observable<ExitType>{return this.http.put<ExitType>(this.url+'/'+id,payload);}
delete(id:number):Observable<void>{return this.http.delete<void>(this.url+'/'+id);}
}
