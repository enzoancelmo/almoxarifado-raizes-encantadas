import{Injectable}from'@angular/core';
import{HttpClient}from'@angular/common/http';
import{Observable}from'rxjs';
import{environment}from'../../../environments/environment';
import{EntryType,EntryTypePayload}from'../models/entry-type.model';
@Injectable({providedIn:'root'})
export class EntryTypeService{private readonly url=environment.apiUrl+'/entry-types';constructor(private http:HttpClient){}
list(activeOnly=false):Observable<EntryType[]>{return this.http.get<EntryType[]>(this.url+(activeOnly?'?activeOnly=true':''));}
create(payload:EntryTypePayload):Observable<EntryType>{return this.http.post<EntryType>(this.url,payload);}
update(id:number,payload:EntryTypePayload):Observable<EntryType>{return this.http.put<EntryType>(this.url+'/'+id,payload);}
delete(id:number):Observable<void>{return this.http.delete<void>(this.url+'/'+id);}}
