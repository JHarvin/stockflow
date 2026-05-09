import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { InventoryService } from '../../services/inventory.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-movements',
  standalone: true,
  imports: [
    CommonModule, 
    ReactiveFormsModule, 
    MatFormFieldModule, 
    MatInputModule, 
    MatSelectModule, 
    MatButtonModule,
    MatSnackBarModule
  ],
  templateUrl: './movements.html',
  styleUrls: ['./movements.scss']
})
export class MovementsComponent {
  private fb = inject(FormBuilder);
  private inventoryService = inject(InventoryService);
  private snackBar = inject(MatSnackBar);

  // los productos del signal global para el select
  products = this.inventoryService.products;

  movementForm = this.fb.group({
    productId: [null, Validators.required],
    type: ['IN', Validators.required],
    quantity: [1, [Validators.required, Validators.min(1)]],
    reason: ['', Validators.maxLength(255)]
  });

  async onSubmit() {
    if (this.movementForm.valid) {
      try {
        const val = this.movementForm.value;
        // Llamada al endpoint del api
        await this.inventoryService.registerMovement(val);
        
        this.snackBar.open('Movimiento registrado con éxito', 'Cerrar', { duration: 3000 });
        this.movementForm.reset({ type: 'IN', quantity: 1 });
        
        // se actualiza signals para que el Dashboard cambie de inmediato
        await this.inventoryService.loadProducts(); 
      } catch (error: any) {
        this.snackBar.open(error.error?.message || 'Error al registrar', 'OK', { duration: 5000 });
      }
    }
  }
}
