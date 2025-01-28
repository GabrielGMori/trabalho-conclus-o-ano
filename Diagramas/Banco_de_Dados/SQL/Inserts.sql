INSERT INTO Companhia_Aerea (codigo_icao_companhia, nome_companhia) VALUES
('LAT', 'LATAM Airlines'),
('GOL', 'Gol Linhas Aéreas'),
('AZU', 'Azul Linhas Aéreas'),
('TAP', 'TAP Air Portugal'),
('AMX', 'AeroMexico');

INSERT INTO Aeronave (modelo_aeronave, capacidade_aeronave, assentos_por_fileira_aeronave, id_companhia_aeronave_fk) VALUES
('Airbus A320', 180, 6, 1),
('Boeing 737', 160, 6, 2),
('Embraer E195', 132, 4, 3),
('Airbus A330', 277, 8, 1),
('Boeing 787', 242, 8, 5);

INSERT INTO Manutencao (descricao_manutencao, data_inicio_manutencao, data_fim_manutencao, status_manutencao, id_aeronave_manutencao_fk) VALUES
('Troca de motor', '2025-01-15 08:00:00', '2025-01-20 18:00:00', 'Concluído', 1),
('Revisão de rotina', '2025-01-10 09:00:00', '2025-01-12 15:00:00', 'Concluído', 2),
('Substituição de rodas', '2025-01-18 07:00:00', '2025-01-19 14:00:00', 'Concluído', 3),
('Reparo no sistema hidráulico', '2025-01-22 10:00:00', '2025-01-25 17:00:00', 'Em andamento', 4),
('Atualização de software', '2025-01-25 09:00:00', '2025-01-26 13:00:00', 'Concluído', 5);

INSERT INTO Aeroporto (nome_aeroporto, localizacao_aeroporto) VALUES
('Aeroporto Internacional de Guarulhos', 'São Paulo - SP, Brasil'),
('Aeroporto Internacional do Galeão', 'Rio de Janeiro - RJ, Brasil'),
('Aeroporto de Lisboa', 'Lisboa, Portugal'),
('Aeroporto Internacional de Cancún', 'Cancún, México'),
('Aeroporto Internacional de Miami', 'Miami, EUA');

INSERT INTO Portao_Embarque (codigo_portao, disponivel_portao, id_aeroporto_portao_fk) VALUES
('A1', 1, 1),
('B5', 0, 2),
('C3', 1, 3),
('D7', 1, 4),
('E2', 0, 5);

INSERT INTO Voo (numero_voo, status_voo, origem_voo, destino_voo, horario_embarque_voo, horario_desembarque_voo, id_aeronave_voo_fk, id_portao_embarque_voo_fk, id_aeroporto_chegada_voo_fk) VALUES
('LAT1234', 'Confirmado', 'São Paulo - SP', 'Rio de Janeiro - RJ', '2025-01-30 10:00:00', '2025-01-30 11:15:00', 1, 1, 2),
('GOL5678', 'Cancelado', 'Rio de Janeiro - RJ', 'Lisboa', '2025-02-01 16:00:00', '2025-02-02 05:30:00', 2, 2, 3),
('AZU9101', 'Atrasado', 'Lisboa', 'Cancún', '2025-02-03 13:00:00', '2025-02-03 21:45:00', 3, 3, 4),
('TAP1112', 'Confirmado', 'Cancún', 'Miami', '2025-02-04 09:00:00', '2025-02-04 12:00:00', 4, 4, 5),
('AMX1314', 'Confirmado', 'Miami', 'São Paulo - SP', '2025-02-05 20:00:00', '2025-02-06 05:30:00', 5, 5, 1);

INSERT INTO Pais (nome_pais) VALUES
('Brasil'),
('Portugal'),
('México'),
('Estados Unidos'),
('Canadá');

INSERT INTO Passageiro (cpf_passageiro_pk, email_passageiro, nome_passageiro, senha_passageiro, passaporte_passageiro, telefone_passageiro, id_pais_passageiro_fk) VALUES
('12345678901', 'joao.silva@gmail.com', 'João Silva', 'senha123', 'BR123456', '11987654321', 1),
('98765432100', 'maria.oliveira@gmail.com', 'Maria Oliveira', 'senha456', 'PT654321', '21987654321', 2),
('45678912300', 'carlos.santos@gmail.com', 'Carlos Santos', 'senha789', 'MX789123', '31987654321', 3),
('78912345600', 'ana.lima@gmail.com', 'Ana Lima', 'senha321', 'US321789', '41987654321', 4),
('32165498700', 'lucas.martins@gmail.com', 'Lucas Martins', 'senha654', 'CA987654', '51987654321', 5);

INSERT INTO CheckIn (data_checkin) VALUES
('2025-01-30 08:30:00'),
('2025-02-01 15:30:00'),
('2025-02-03 11:30:00'),
('2025-02-04 07:30:00'),
('2025-02-05 18:30:00');

INSERT INTO Metodo_Pagamento (metodo_pagamento) VALUES
('Cartão de Crédito'),
('Cartão de Débito'),
('Transferência Bancária'),
('PayPal'),
('Pix');

INSERT INTO Passagem (data_compra_passagem, assento_passagem, cpf_passageiro_passagem_fk, id_voo_passagem_fk, id_metodo_pagamento_passagem_fk, id_checkin_passagem_fk) VALUES
('2025-01-28 15:00:00', '12A', '12345678901', 1, 1, 1),
('2025-01-29 14:30:00', '5B', '98765432100', 2, 2, 2),
('2025-01-30 10:15:00', '18C', '45678912300', 3, 3, 3),
('2025-01-31 09:45:00', '2D', '78912345600', 4, 4, 4),
('2025-02-01 16:00:00', '9E', '32165498700', 5, 5, 5);

INSERT INTO Administrador (cpf_administrador_pk, nome_administrador, senha_administrador) VALUES
('00000000001', 'Administrador 1', 'admin123'),
('00000000002', 'Administrador 2', 'admin456'),
('00000000003', 'Administrador 3', 'admin789'),
('00000000004', 'Administrador 4', 'admin321'),
('00000000005', 'Administrador 5', 'admin654');
