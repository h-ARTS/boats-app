import { ChangeDetectionStrategy, Component, signal, computed } from '@angular/core';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { BoatService } from '../boats/services/boat.service';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../../../shared/auth/auth.service';
import { BoatList } from '../boats/components/boat-list/boat-list.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.page.html',
  styleUrl: './home.page.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatCardModule, MatButtonModule, MatIconModule, BoatList],
})
export class HomePage {
  private router = inject(Router);
  private auth = inject(AuthService);

  goToAdd() {
    this.router.navigate(['/boats/add']);
  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
