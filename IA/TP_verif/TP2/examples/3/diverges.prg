int diverges(int x)
{
  int r = x;
  # la vérification de la termination doit échouer
  while (r >= 0) {
    r = r + 1;
  }
  return r;
}