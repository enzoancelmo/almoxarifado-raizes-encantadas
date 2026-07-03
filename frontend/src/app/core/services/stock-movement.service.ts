import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { StockMovement,StockMovementPayload } from '../models/stock-movement.model';
@Injectable({providedIn:'root'})
export class StockMovementService{
 private readonly url=environment.apiUrl+'/stock-movements'; constructor(private readonly http:HttpClient){}
 list():Observable<StockMovement[]>{return this.http.get<StockMovement[]>(this.url);}
 findById(id:number):Observable<StockMovement>{return this.http.get<StockMovement>(this.url+'/'+id);}
 listByProduct(productId:number):Observable<StockMovement[]>{return this.http.get<StockMovement[]>(this.url+'/product/'+productId);}
 create(payload:StockMovementPayload):Observable<StockMovement>{return this.http.post<StockMovement>(this.url,payload);}
}
