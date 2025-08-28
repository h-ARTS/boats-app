import { ChangeDetectionStrategy, Component, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { inject } from '@angular/core';
import { BoatService } from '../services/boat.service';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { Boat } from '../../../../shared/types';

@Component({
  selector: 'app-boat-form',
  templateUrl: './boat-form.component.html',
  styleUrl: './boat-form.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
  ],
})
export class BoatFormComponent {
  private fb = inject(FormBuilder);
  private boatService = inject(BoatService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  id = Number(this.route.snapshot.paramMap.get('id'));
  isEdit = signal(!!this.id);
  loading = signal(false);
  error = signal<string | null>(null);

  form = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(3)]],
    type: ['', [Validators.required, Validators.minLength(3)]],
    length: [0, [Validators.required, Validators.min(1)]],
    description: ['', [Validators.required, Validators.minLength(10)]],
  });

  constructor() {
    if (this.isEdit()) {
      this.boatService.getBoat(this.id).subscribe((boat) => {
        if (boat) this.form.patchValue(boat as Boat);
      });
    }
  }

  onSubmit() {
    if (this.form.invalid) return;
    this.loading.set(true);
    this.error.set(null);
    const boat: Boat = {
      id: this.id ?? Math.random().toString(36).substring(2, 9),
      ...(this.form.value as Omit<Boat, 'id'>),
    };
    const obs = this.isEdit()
      ? this.boatService.updateBoat(boat.id, boat)
      : this.boatService.addBoat(boat);
    obs.subscribe({
      next: () => this.router.navigate(['/home']),
      error: () => {
        this.error.set('Save failed');
        this.loading.set(false);
      },
      complete: () => this.loading.set(false),
    });
  }

  goBack() {
    this.router.navigate(['/home']);
  }
}
