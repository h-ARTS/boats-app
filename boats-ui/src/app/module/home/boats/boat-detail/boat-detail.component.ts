import { ChangeDetectionStrategy, Component, computed, signal } from '@angular/core';
import { inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatButtonModule } from '@angular/material/button';
import { toSignal } from '@angular/core/rxjs-interop';
import { BoatNotFound } from '../components/boat-not-found/boat-not-found.component';
import { Boat } from '../../../../shared/types';

type View = { kind: 'loading' } | { kind: 'loaded'; boat: Boat } | { kind: 'not-found' };

@Component({
  selector: 'app-boat-detail',
  templateUrl: './boat-detail.component.html',
  styleUrl: './boat-detail.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatCardModule, MatIconModule, MatProgressSpinnerModule, MatButtonModule, BoatNotFound],
})
export class BoatDetailComponent {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private data = toSignal(this.route.data);

  private isBoat = (b: any): b is Boat =>
    !!b &&
    typeof b === 'object' &&
    'name' in (b as any) &&
    'type' in (b as any) &&
    'length' in (b as any);

  id = this.route.snapshot.paramMap.get('id') ?? '';
  boat = computed(() => this.data()?.['boat'] as Boat | null);

  view = computed<View>(() => {
    const _data = this.data();
    if (!_data) return { kind: 'loading' };

    return this.isBoat(this.boat())
      ? { kind: 'loaded', boat: this.boat() as Boat }
      : { kind: 'not-found' };
  });

  goBack() {
    this.router.navigate(['/home']);
  }

  goToEdit() {
    this.router.navigate(['boats/edit', this.id]);
  }
}
