import { Exchange } from "./exchange";
import { Period } from "./period";


export class Company {
    id: number;
    name: string;
    symbol: string;
    exchange: Exchange;

    price: number;
    buyBelowPrice : number;
    sellAbovePrice: number;
       
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
}