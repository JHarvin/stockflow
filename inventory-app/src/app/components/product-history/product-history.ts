import { Component,input  } from '@angular/core';

@Component({
  selector: 'app-product-history',
  imports: [],
  standalone: true,
  templateUrl: './product-history.html',
  styleUrl: './product-history.scss',
})
export class ProductHistory {
  //input de escucha para ver el producto historial
 productId = input.required<number>(); 
}
