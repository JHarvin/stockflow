import { Component, inject, OnInit, computed, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InventoryService } from '../../services/inventory.service';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { ProductHistory } from '../product-history/product-history';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatTableModule, MatButtonModule, ProductHistory, MatProgressSpinnerModule],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.scss']
})
export class DashboardComponent implements OnInit {
  private inventoryService = inject(InventoryService);
  public isLoading = this.inventoryService.loading;
  // Signals de estado
  selectedProductId = signal<number | null>(null);

  // cargar datos de la API
  products = this.inventoryService.products;
  
  // Signals de estado
  totalProducts = computed(() => this.products().length);
  criticalCount = computed(() => 
    this.products().filter(p => p.currentStock <= p.minStock).length
  );

  ngOnInit() {
    this.inventoryService.loadProducts();
  }

  selectProduct(id: number) {
    console.log('ID seleccionado:', id);
    this.selectedProductId.set(id);
  }
}
