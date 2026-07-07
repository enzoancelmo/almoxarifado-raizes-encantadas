import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { EventTemplate, EventTemplateItemPayload, EventTemplatePayload } from '../models/event-template.model';

@Injectable({ providedIn: 'root' })
export class EventTemplateService {
  private readonly url = environment.apiUrl + '/event-templates';
  constructor(private readonly http: HttpClient) {}
  list(): Observable<EventTemplate[]> { return this.http.get<EventTemplate[]>(this.url); }
  get(id: number): Observable<EventTemplate> { return this.http.get<EventTemplate>(this.url + '/' + id); }
  create(payload: EventTemplatePayload): Observable<EventTemplate> { return this.http.post<EventTemplate>(this.url, payload); }
  update(id: number, payload: EventTemplatePayload): Observable<EventTemplate> { return this.http.put<EventTemplate>(this.url + '/' + id, payload); }
  deactivate(id: number): Observable<void> { return this.http.delete<void>(this.url + '/' + id); }
  addItem(templateId: number, payload: EventTemplateItemPayload): Observable<EventTemplate> { return this.http.post<EventTemplate>(`${this.url}/${templateId}/items`, payload); }
  updateItem(templateId: number, itemId: number, payload: EventTemplateItemPayload): Observable<EventTemplate> { return this.http.put<EventTemplate>(`${this.url}/${templateId}/items/${itemId}`, payload); }
  deleteItem(templateId: number, itemId: number): Observable<EventTemplate> { return this.http.delete<EventTemplate>(`${this.url}/${templateId}/items/${itemId}`); }
}