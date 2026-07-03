import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Product, ProductPayload } from '../models/product.model';

@Injectable({providedIn:'root'})
export class ProductService {
  private readonly url = environment.apiUrl + '/products';
  constructor(private readonly http: HttpClient) {}
  list(): Observable<Product[]> { return this.http.get<Product[]>(this.url); }
  get(id:number): Observable<Product> { return this.http.get<Product>(this.url + '/' + id); }
  create(payload:ProductPayload): Observable<Product> { return this.http.post<Product>(this.url, payload); }
  update(id:number, payload:ProductPayload): Observable<Product> { return this.http.put<Product>(this.url + '/' + id, payload); }
  delete(id:number): Observable<void> { return this.http.delete<void>(this.url + '/' + id); }
}
