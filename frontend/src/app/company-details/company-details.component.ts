import { Component, OnInit, Input } from '@angular/core';
import {Company} from '../model/company';

@Component({
  selector: 'app-company-details',
  templateUrl: './company-details.component.html',
  styleUrls: ['./company-details.component.css']
})
export class CompanyDetailsComponent implements OnInit {
  @Input() detailedCompany: Company;

  constructor() { }

  ngOnInit() {

  }

}
