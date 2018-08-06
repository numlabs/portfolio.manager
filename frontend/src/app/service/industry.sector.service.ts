import { Injectable } from '@angular/core';
import { IndustrySector } from '../model/industry.sector';
import { Observable, of } from 'rxjs';
import { MessageService } from './message.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class IndustrySectorService {
  private allSectorsUrl = '/portfoliomng/industrysector/list/';  // URL to web api

  constructor(private messageService: MessageService, private http: HttpClient) { }

  getIndustrySectors(): Observable<IndustrySector[]> {
    this.messageService.add('IndustrySectorService: fetching sectors ');
    return this.http.get<IndustrySector[]>(this.allSectorsUrl);    
  }

}
