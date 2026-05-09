import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Product } from '../models/product';
import { firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class InventoryService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/v1';

  
  products = signal<Product[]>([]);
  loading = signal<boolean>(false);

  async loadProducts() {
    this.loading.set(true);
    try {
      const data = await firstValueFrom(this.http.get<Product[]>(`${this.apiUrl}/products`));
      this.products.set(data);
    } finally {
      this.loading.set(false);
    }
  }

  
  async getAlerts() {
    return firstValueFrom(this.http.get<Product[]>(`${this.apiUrl}/alerts`));
  }
}

