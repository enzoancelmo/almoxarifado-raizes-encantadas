import{Injectable}from'@angular/core';
import{HttpClient}from'@angular/common/http';
import{Observable}from'rxjs';
import{environment}from'../../../environments/environment';
import{QuickEntryPayload,QuickEntryResponse}from'../models/quick-entry.model';
@Injectable({providedIn:'root'})
export class QuickEntryService{private readonly url=environment.apiUrl+'/quick-entry';constructor(private http:HttpClient){}create(payload:QuickEntryPayload):Observable<QuickEntryResponse>{return this.http.post<QuickEntryResponse>(this.url,payload);}}
