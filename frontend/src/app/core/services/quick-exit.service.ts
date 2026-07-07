import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { QuickExitPayload, QuickExitResponse } from '../models/quick-exit.model';

@Injectable({ providedIn: 'root' })
export class QuickExitService {
  private readonly url = environment.apiUrl + '/quick-exit';
  constructor(private readonly http: HttpClient) {}
  create(payload: QuickExitPayload): Observable<QuickExitResponse> { return this.http.post<QuickExitResponse>(this.url, payload); }
}