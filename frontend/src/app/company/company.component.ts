import { Component, OnInit } from '@angular/core';
import { ExchangeService } from '../service/exchange.service'
import { Exchange} from "../model/exchange"
import { Company} from "../model/company"
import { IndustrySector } from '../model/industry.sector';
import { IndustrySectorService } from '../service/industry.sector.service';
import { CompanyService } from '../service/company.service';

@Component({
  selector: 'app-company',
  templateUrl: './company.component.html',
  styleUrls: ['./company.component.css']
})
export class CompanyComponent implements OnInit {
  exchanges: Exchange[];
  industrySectors: IndustrySector[]; 
  company = new Company();

  messageSuccess: string = null;
  messageInfo: string = null;
  messageWarning: string = null;
  messageError: string = null;

  constructor(private exchangeService: ExchangeService, private industrySectorService: IndustrySectorService, private companyService: CompanyService ) { }

  ngOnInit() {
    this.exchangeService.getExchanges().subscribe(exchanges => this.exchanges = exchanges); 
    this.industrySectorService.getIndustrySectors().subscribe(industrySectors => this.industrySectors = industrySectors); 
    console.log(this.industrySectors); 
  }

  addCompany() {
    this.resetMessages(); 

    if(this.company.name === "") {
        this.messageError = "Name field missing!" ;
    }

    if(this.company.tickerSymbol === "") {
      this.messageError += "Ticker symbol field missing!";
    }

    if(this.company.price == 0 || this.company.price == null) {
      this.messageError += "Price field missing!";
    }

    if(this.company.exchange == null) {
      this.messageError += "Exchange field missing!";
    }

    if(this.company.industrySector == null) {
      this.messageError += "Industry sector field missing!";
    }

    if(this.messageError == null) {
      this.companyService.addCompany(this.company).subscribe(
        res => { this.messageInfo = res },
        err => { this.messageError = "Error ocured when adding the company!"}
      );
    }

    console.log(this.company.exchange.id);
  }

  resetMessages() {
    this.messageSuccess = null;
    this.messageInfo = null;
    this.messageWarning = null;
    this.messageError = null;
  }
}
