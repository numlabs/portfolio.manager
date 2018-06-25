import { Component, OnInit } from '@angular/core';
import { Company } from '../model/company'
import { COMPANIES } from '../service/companies.service'
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

  constructor(private companyService: CompanyService, private route: ActivatedRoute, private location: Location, private messageService: MessageService) { }

  ngOnInit() {
    this.messageService.add('CompaniesComponent: initing');
      this.getCompanies();
  }

  onSelect(company: Company): void {
    this.getCompanies();
  }

  getCompanies(): void {
    let exchange: string = this.route.snapshot.paramMap.get('exchange');
    this.companyService.getCompanies(exchange).subscribe(companies => this.companies = companies);
  }


}
