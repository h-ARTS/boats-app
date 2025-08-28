import { ChangeDetectionStrategy, Component, signal, computed } from '@angular/core';
import { inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BoatService } from '../boats/services/boat.service';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { AuthService } from '../../../shared/auth/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.page.html',
  styleUrl: './home.page.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatCardModule, MatButtonModule, MatIconModule, MatTableModule],
})
export class HomePage {
  private boatService = inject(BoatService);
  private router = inject(Router);
  private auth = inject(AuthService);

  protected boats = this.boatService.boats;

  protected displayedColumns = ['name', 'actions'];

  constructor() {
    this.boatService.loadBoats();
  }

  goToAdd() {
    this.router.navigate(['/boats/add']);
  }

  goToEdit(id: number) {
    this.router.navigate(['/boats/edit', id]);
  }

  goToDetail(id: number) {
    this.router.navigate(['/boats/detail', id]);
  }

  deleteBoat(id: number) {
    this.boatService.deleteBoat(id);
  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
