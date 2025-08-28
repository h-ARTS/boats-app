import { Component, inject } from '@angular/core';
import { BoatService } from '../../services/boat.service';
import { MatCardContent, MatCardHeader, MatCardModule } from '@angular/material/card';
import { Router } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-boat-list',
  imports: [
    MatTableModule,
    MatIconModule,
    MatButtonModule,
    MatCardContent,
    MatCardHeader,
    MatCardModule,
  ],
  templateUrl: './boat-list.component.html',
  styleUrls: ['./boat-list.component.scss'],
})
export class BoatList {
  private boatService = inject(BoatService);
  private router = inject(Router);

  protected boats = this.boatService.boats;

  protected displayedColumns = ['name', 'actions'];

  goToEdit(id: number) {
    this.router.navigate(['/boats/edit', id]);
  }

  goToDetail(id: number) {
    this.router.navigate(['/boats/detail', id]);
  }

  deleteBoat(id: number) {
    this.boatService.deleteBoat(id);
  }
}
