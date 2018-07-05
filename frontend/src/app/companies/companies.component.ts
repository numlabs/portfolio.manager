import { Component, OnInit } from '@angular/core';
import { Company } from '../model/company'
//import { COMPANIES } from '../service/companies.service'
import { CompanyService } from '../service/company.service'
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { MessageService } from '../service/message.service';

@Component({
  selector: 'app-companies',
  templateUrl: './companies.component.html',
  styleUrls: ['./companies.component.css']
})
export class CompaniesComponent implements OnInit {
  companies: Company[];
  selectedCompany: Company;

  constructor(private companyService: CompanyService, private route: ActivatedRoute, private location: Location, private messageService: MessageService) {      
  }

  ngOnInit() { 
    this.route.params.subscribe( params => this.getCompanies(params['exchange'])); 
  }

  onSelect(company: Company): void {
    let exchange: string = this.route.snapshot.paramMap.get('exchange');
    this.selectedCompany = company;
    console.log(this.selectedCompany);   
  }

  getCompanies(exchange): void {     
    console.log("Loading for exchange: " + exchange);
    this.companyService.getCompanies(exchange).subscribe(companies => this.companies = companies);
  }

}
