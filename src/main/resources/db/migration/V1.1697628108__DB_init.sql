CREATE TABLE IF NOT EXISTS public.client (
                        id BIGSERIAL PRIMARY KEY,
                        first_name VARCHAR(50) NOT NULL,
                        last_name VARCHAR(50) NOT NULL,
                        email VARCHAR(100) NOT NULL

);

CREATE TABLE IF NOT EXISTS public.payment_system (
                                           id BIGSERIAL PRIMARY KEY,
                                           payment_system_name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS public.card (
                      id BIGSERIAL PRIMARY KEY,
                      card_number VARCHAR(20) NOT NULL,
                      expiration_date TIMESTAMP NOT NULL,
                      payment_system_id BIGINT NOT NULL REFERENCES payment_system(id),
                      client_id BIGINT REFERENCES public.client(id) ON DELETE CASCADE,
                      balance INTEGER NOT NULL DEFAULT 0,
                      created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS public.processing_center (
                                  id BIGSERIAL PRIMARY KEY,
                                  processing_center_name VARCHAR(50) NOT NULL,
                                  address VARCHAR(100) NOT NULL,
                                  contact_info TEXT,
                                  country VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS public.transaction (
                             id BIGSERIAL PRIMARY KEY,
                             payment_amount INTEGER NOT NULL,
                             sender_card_id BIGINT REFERENCES public.card(id) ON DELETE CASCADE,
                             receiver_card_id BIGINT REFERENCES public.card(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.transaction_history (
                                    id BIGSERIAL PRIMARY KEY,
                                    transaction_id INTEGER REFERENCES public.transaction(id) ON DELETE CASCADE,
                                    client_id BIGINT REFERENCES public.client(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.payment (
                         id BIGSERIAL PRIMARY KEY,
                         payment_date TIMESTAMP NOT NULL,
                         payment_amount INTEGER NOT NULL,
                         sender_card_id BIGINT REFERENCES public.card(id) ON DELETE CASCADE,
                         receiver_card_id BIGINT REFERENCES public.card(id) ON DELETE CASCADE,
                         payment_system_id BIGINT REFERENCES public.payment_system(id) ON DELETE CASCADE ,
                         is_confirmed BOOLEAN NOT NULL DEFAULT FALSE
);