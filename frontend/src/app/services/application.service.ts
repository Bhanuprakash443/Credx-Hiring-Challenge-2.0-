import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApplicationService {
  private apiUrl = '/api/applications';

  constructor(private http: HttpClient) {}

  apply(jobId: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}?jobId=${jobId}`, {});
  }

  getApplications(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  updateStatus(id: number, status: string, notes?: string): Observable<any> {
    let url = `${this.apiUrl}/${id}/status?status=${status}`;
    if (notes) {
      url += `&notes=${encodeURIComponent(notes)}`;
    }
    return this.http.put<any>(url, {});
  }
}
