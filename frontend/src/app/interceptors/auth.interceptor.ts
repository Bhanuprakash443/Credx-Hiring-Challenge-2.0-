import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // Bypasses adding auth headers for external URLs (if any)
  if (!req.url.includes('/api/')) {
    return next(req);
  }

  const currentUserStr = localStorage.getItem('currentUser');
  if (currentUserStr) {
    try {
      const user = JSON.parse(currentUserStr);
      if (user && user.id && user.role) {
        const clonedRequest = req.clone({
          setHeaders: {
            'X-User-Id': String(user.id),
            'X-User-Role': String(user.role)
          }
        });
        return next(clonedRequest);
      }
    } catch (e) {
      // LocalStorage parsing error, proceed without headers
    }
  }

  return next(req);
};
