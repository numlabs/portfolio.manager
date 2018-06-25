import { Injectable } from '@angular/core';
import { Company } from '../model/company';
import { COMPANIES } from '../service/companies.service';
import { COMPANIES_TR } from '../service/companies.service';
import { Observable, of } from 'rxjs';
import { MessageService } from './message.service';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {

  constructor(private messageService: MessageService) { }

  getCompanies(exchange: string): Observable<Company[]> {
    this.messageService.add('CompanyService: fetched companies' + exchange);

    if(exchange == 'bist') {
      return of(COMPANIES_TR);
    } 
    
    return of(COMPANIES);
  }

}
