import { Exchange } from "./exchange";
import { Period } from "./period";
import { IndustrySector } from "./industry.sector";


export class Company {
    id: number;
    name: string;
    tickerSymbol: string;
    exchange: Exchange;
    industrySector: IndustrySector;

    price: number;
       
    stockUrl: string;
    description: string;
    periods: Period[];
    
    pe: number; // price to earnings
    pToEbit: number; // price to EBIT
    pb: number; // price to book value
    evtoEBIT: number; // EBIT to Enterprice value

    equity: number;
    ev: number; // enterprise value
    marketCap: number;

    constructor() {
        this.name = "";
        this.id = 0;
        this.tickerSymbol = "";
        this.exchange = null;
        this.price = 0;
        this.stockUrl = "";
        this.description = "";
        this.periods = null;
        this.pb = 0;
        this.pe = 0;
        this.pToEbit = 0;
        this.ev = 0;
        this.evtoEBIT = 0;
        this.equity = 0;
        this.marketCap = 0;
        this.industrySector = null;
    }
}