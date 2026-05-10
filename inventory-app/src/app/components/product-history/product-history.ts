import { Component, input, inject, effect, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InventoryService } from '../../services/inventory.service';

@Component({
  selector: 'app-product-history',
  standalone: true,
  imports: [CommonModule],
  styleUrls: ['./product-history.scss'],
  template: `
    <div class="history-list">
  @for (item of history(); track item.id) {
    <div class="history-item" [ngClass]="item.type.toLowerCase()">
      <div class="history-header">
        <span class="type-badge">{{ item.type === 'IN' ? 'ENTRADA' : 'SALIDA' }}</span>
        <span class="quantity"><strong>{{ item.quantity }}</strong> unidades</span>
        <span class="date">{{ item.timestamp | date:'dd/MM/yyyy HH:mm' }}</span>
      </div>
      <p class="reason">{{ item.reason || 'Sin motivo especificado' }}</p>
    </div>
  } @empty {
    <p class="empty-msg">No hay movimientos para este producto.</p>
  }
</div>

  `
})
export class ProductHistory {
   private inventoryService = inject(InventoryService);
  
  // Signals de estado
  productId = input.required<number>();
  history = signal<any[]>([]);

  constructor() {
    // cada vez que el productId cambie, 
    // automáticamente pide los datos al servicio.
    effect(() => {
      const id = this.productId();
      if (id) {
        this.loadHistory(id);
      }
    });
  }

  async loadHistory(id: number) {
    try {
      const data = await this.inventoryService.getHistoryByProduct(id);
      this.history.set(data);
    } catch (error) {
      console.error('Error al cargar historial', error);
    }
  }
}
