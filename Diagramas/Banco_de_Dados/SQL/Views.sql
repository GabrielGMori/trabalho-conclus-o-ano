CREATE OR REPLACE VIEW Manutencoes_View AS
SELECT
    manutencao.id_manutencao_pk AS id_manutencao,
    manutencao.descricao_manutencao AS descricao,
    manutencao.data_inicio_manutencao AS data_inicio,
    manutencao.data_fim_manutencao AS data_fim,
    manutencao.status_manutencao AS status,
    aeronave.id_aeronave_pk as id_aeronave,
    aeronave.modelo_aeronave AS aeronave,
    companhia.id_companhia_pk as id_companhia_aerea,
    companhia.nome_companhia AS companhia

FROM Manutencao manutencao
JOIN Aeronave aeronave ON manutencao.id_aeronave_manutencao_fk = aeronave.id_aeronave_pk
JOIN Companhia_Aerea companhia ON aeronave.id_companhia_aeronave_fk = companhia.id_companhia_pk
ORDER BY manutencao.data_inicio_manutencao;


CREATE OR REPLACE VIEW Aeronaves_View AS
SELECT
    aeronave.id_aeronave_pk as id_aeronave,
    aeronave.modelo_aeronave AS aeronave,
    aeronave.capacidade_aeronave AS capacidade,
    aeronave.assentos_por_fileira_aeronave AS assentos_por_fileira,
    companhia.id_companhia_pk as id_companhia_aerea,
    companhia.nome_companhia AS companhia

FROM Aeronave aeronave
JOIN Companhia_Aerea companhia ON aeronave.id_companhia_aeronave_fk = companhia.id_companhia_pk
ORDER BY aeronave.modelo_aeronave;


CREATE OR REPLACE VIEW Voos_View AS 
SELECT 
    voo.id_voo_pk as id_voo,
    voo.numero_voo as numero,
    voo.status_voo as status,
    voo.origem_voo as origem,
    voo.destino_voo as destino,
    voo.horario_embarque_voo as horario_embarque,
    voo.horario_desembarque_voo as horario_desembarque,
    aeronave.id_aeronave_pk as id_aeronave,
    aeronave.modelo_aeronave as aeronave,
    companhia.id_companhia_pk as id_companhia_aerea,
    companhia.nome_companhia as companhia_aerea,
    portao.id_portao_pk as id_portao_de_embarque,
    portao.codigo_portao as portao_de_embarque,
    aeroporto_embarque.id_aeroporto_pk as id_aeroporto_embarque,
    aeroporto_embarque.nome_aeroporto as aeroporto_embarque,
    aeroporto_desembarque.id_aeroporto_pk as id_aeroporto_desembarque,
    aeroporto_desembarque.nome_aeroporto as aeroporto_desembarque
    
FROM Voo voo
JOIN Aeronave aeronave ON voo.id_aeronave_voo_fk = aeronave.id_aeronave_pk
JOIN Companhia_Aerea companhia ON aeronave.id_companhia_aeronave_fk = companhia.id_companhia_pk
JOIN Portao_Embarque portao ON voo.id_portao_embarque_voo_fk = portao.id_portao_pk
JOIN Aeroporto aeroporto_embarque ON portao.id_aeroporto_portao_fk = aeroporto_embarque.id_aeroporto_pk 
JOIN Aeroporto aeroporto_desembarque ON voo.id_aeroporto_chegada_voo_fk = aeroporto_desembarque.id_aeroporto_pk 
ORDER BY voo.horario_embarque_voo;


CREATE OR REPLACE VIEW Portoes_Embarque_View AS
SELECT
    portao.id_portao_pk as id_portao_de_embarque,
    portao.codigo_portao as codigo,
    portao.disponivel_portao as disponivel,
    aeroporto.id_aeroporto_pk as id_aeroporto,
    aeroporto.nome_aeroporto as aeroporto

FROM Portao_Embarque portao
JOIN Aeroporto aeroporto ON portao.id_aeroporto_portao_fk = aeroporto.id_aeroporto_pk
ORDER BY aeroporto.nome_aeroporto, portao.codigo_portao;


CREATE OR REPLACE VIEW Passagens_View AS 
SELECT
	passagem.id_passagem_pk as id_passagem,
    passagem.data_compra_passagem as data_compra,
    passagem.assento_passagem as assento_reservado,
    passagem.id_checkin_passagem_fk as id_check_in,
    passageiro.cpf_passageiro_pk as cpf_passageiro,
    passageiro.nome_passageiro as nome_passageiro,
    voo.id_voo_pk as id_voo,
    voo.numero_voo as numero_voo,
    companhia.id_companhia_pk as id_companhia,
    companhia.nome_companhia as companhia_aerea,
    metodo.id_metodo_pagamento_pk as id_metodo_pagamento,
    metodo.metodo_pagamento as metodo_pagamento,
    portao.id_portao_pk as id_portao_de_embarque,
    portao.codigo_portao as portao_de_embarque
    
FROM Passagem passagem
JOIN Passageiro passageiro ON passagem.cpf_passageiro_passagem_fk = passageiro.cpf_passageiro_pk
JOIN Voo voo ON passagem.id_voo_passagem_fk = voo.id_voo_pk
JOIN Portao_Embarque portao ON voo.id_portao_embarque_voo_fk = portao.id_portao_pk 
JOIN Aeronave aeronave ON voo.id_aeronave_voo_fk = aeronave.id_aeronave_pk
JOIN Companhia_Aerea companhia ON aeronave.id_companhia_aeronave_fk = companhia.id_companhia_pk
JOIN Metodo_Pagamento metodo ON passagem.id_metodo_pagamento_passagem_fk = metodo.id_metodo_pagamento_pk
ORDER BY voo.horario_embarque_voo;


CREATE OR REPLACE VIEW Passageiros_View AS
SELECT
    passageiro.cpf_passageiro_pk as cpf,
    passageiro.email_passageiro as email,
    passageiro.nome_passageiro as nome,
    passageiro.senha_passageiro as senha,
    passageiro.passaporte_passageiro as passaporte,
    passageiro.telefone_passageiro as telefone,
    pais.id_pais_pk as id_pais,
    pais.nome_pais as pais

FROM Passageiro passageiro
JOIN Pais pais ON passageiro.id_pais_passageiro_fk = pais.id_pais_pk
ORDER BY passageiro.nome_passageiro;
