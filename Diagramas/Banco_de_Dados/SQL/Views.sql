CREATE OR REPLACE VIEW voos AS 
SELECT 
    voo.id_voo_pk as id_voo,
    voo.origem_voo as origem,
    voo.destino_voo as destino,
    voo.horario_partida_voo as horario_partida,
    voo.horario_chegada_voo as horario_chegada,
    aeroporto.id_aeroporto_pk as id_aeroporto,
    aeroporto.nome_aeroporto as aeroporto,
    portao.id_portao_pk as id_portao_de_embarque,
    portao.codigo_portao as portao_de_embarque,
    companhia.id_companhia_pk as id_companhia_aerea,
    companhia.nome_companhia as companhia_aerea,
    voo.numero_voo as numero,
    voo.status_voo as status,
    aeronave.id_aeronave_pk as id_aeronave,
    aeronave.modelo_aeronave as aeronave
    
FROM Voo voo
JOIN Aeronave aeronave ON voo.id_aeronave_voo_fk = aeronave.id_aeronave_pk
JOIN Companhia_Aerea companhia ON aeronave.id_companhia_aeronave_fk = companhia.id_companhia_pk
JOIN Portao_Embarque portao ON voo.id_portao_voo_fk = portao.id_portao_pk
JOIN Aeroporto aeroporto ON portao.id_aeroporto_portao_fk = aeroporto.id_aeroporto_pk
ORDER BY horario_partida_voo DESC;

CREATE OR REPLACE VIEW manutencoes AS 
SELECT
    manutencao.id_manutencao_pk AS id_manutencao,
    manutencao.descricao_manutencao AS descricao,
    aeronave.id_aeronave_pk as id_aeronave,
    aeronave.modelo_aeronave AS aeronave,
    companhia.id_companhia_pk as id_companhia_aerea,
    companhia.nome_companhia AS companhia,
    manutencao.data_inicio_manutencao AS data_inicio,
    manutencao.data_fim_manutencao AS data_fim,
    manutencao.status_manutencao AS status

FROM Manutencao manutencao
JOIN Aeronave aeronave ON manutencao.id_aeronave_manutencao_fk = aeronave.id_aeronave_pk
JOIN Companhia_Aerea companhia ON aeronave.id_companhia_aeronave_fk = companhia.id_companhia_pk
ORDER BY manutencao.data_inicio_manutencao DESC;

CREATE OR REPLACE VIEW passagens AS 
SELECT
	passagem.id_passagem_pk as id_passagem,
    voo.id_voo_pk as id_voo,
    voo.numero_voo as numero_voo,
    companhia.id_companhia_pk as id_companhia,
    companhia.nome_companhia as companhia_aerea,
    passageiro.cpf_passageiro_pk as cpf_passageiro,
    passageiro.nome_passageiro as nome_passageiro,
    pais.id_pais_pk as id_pais,
    pais.nome_pais as nacionalidade_passageiro,
    passagem.assento_passagem as assento_reservado,
    portao.id_portao_pk as id_portao_de_embarque,
    portao.codigo_portao as portao_de_embarque,
    passagem.data_compra_passagem as data_compra,
    metodo.id_metodo_pagamento_pk as id_metodo_pagamento,
    metodo.metodo_pagamento as metodo_pagamento
    
FROM Passagem passagem
JOIN Passageiro passageiro ON passagem.cpf_passageiro_passagem_fk = passageiro.cpf_passageiro_pk
JOIN Pais pais ON passageiro.id_pais_passageiro_fk = pais.id_pais_pk
JOIN Voo voo ON passagem.id_voo_passagem_fk = voo.id_voo_pk
JOIN Portao_Embarque portao ON voo.id_portao_voo_fk = portao.id_portao_pk 
JOIN Aeronave aeronave ON voo.id_aeronave_voo_fk = aeronave.id_aeronave_pk
JOIN Companhia_Aerea companhia ON aeronave.id_companhia_aeronave_fk = companhia.id_companhia_pk
JOIN Metodo_Pagamento metodo ON passagem.id_metodo_pagamento_passagem_fk = metodo.id_metodo_pagamento_pk
ORDER BY data_compra_passagem DESC;
