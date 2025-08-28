import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { BoatService } from '../services/boat.service';
import { Boat } from '../../../../shared/types';

export const boatResolver: ResolveFn<Boat | null> = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
) => {
  const boatService = inject(BoatService);
  const id = route.paramMap.get('id') ?? '';
  if (!id) {
    return of(null);
  }
  return boatService.getBoat(+id).pipe(
    map((boat) => boat),
    catchError(() => of(null))
  );
};
