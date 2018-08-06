import { Injectable } from '@angular/core';
import { Company } from '../model/company';
import { Observable, of } from 'rxjs';
import { MessageService } from './message.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Headers, RequestOptions } from '@angular/http';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {
  private companiesUrl = '/portfoliomng/companies/detailed/';  // URL to web api
  private addCompanyUrl = '/portfoliomng/company/add/';

  constructor(private messageService: MessageService, private http: HttpClient) { }

  getCompanies(exchange: string): Observable<Company[]> {
    this.messageService.add('CompanyService: fetched companies' + exchange);
       
    return this.http.get<Company[]>(this.companiesUrl + exchange);
  }

  updateCompanyPrices(exchange: string) : Observable<string> {  
    console.log("in com sev.");  
    this.messageService.add("Updating prices.");
    return this.http.get<string>('/portfoliomng/companies/update/prices/' + exchange);
  }

  addCompany(company: Company) : Observable<string> {
    let headers = new Headers({ 'Content-Type': 'application/json' });
    let options = new RequestOptions({ headers: headers });
    console.log("Adding the company");
    return this.http.post<string>(this.addCompanyUrl,
       {
         id: null,
         name: company.name,
         tickerSymbol: company.tickerSymbol,
         description: company.description,
         price: company.price,
         stockUrl: company.stockUrl,
         exchange: {
           id: company.exchange
         },
         industrySector: {
           id: company.industrySector
          }     
      }    
    );
  }

}
