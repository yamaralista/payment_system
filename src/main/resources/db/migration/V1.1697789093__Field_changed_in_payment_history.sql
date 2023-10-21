alter table public.payment drop column is_confirmed;

alter table public.payment add column payment_state varchar;
