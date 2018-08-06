import { Injectable } from '@angular/core';
import { Exchange } from '../model/exchange';
import { Observable, of } from 'rxjs';
import { MessageService } from './message.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ExchangeService {
  private exchangesUrl = '/portfoliomng/exchanges/';  // URL to web api

  constructor(private messageService: MessageService, private http: HttpClient) { }

  getExchanges(): Observable<Exchange[]> {
    this.messageService.add('ExchangeService: fetching exchanges ');
    let exchanges = this.http.get<Exchange[]>(this.exchangesUrl);
    
    return exchanges;
  }

}
