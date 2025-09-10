1. O elevador começa parado no térreo (Andar Zero), com as portas abertas;
2. Ao mover o elevador, deve-se atualizar o seu status (subindo ou descendo);
3. O elevador deve levar em consideração se está descendo ou subindo para definir a
rota, ou seja, se estiver subindo deve subir até o ultimo andar definido antes de
começar a descer;
4. A porta do elevador só se fecha quando há uma rota a seguir;
5. A rota deve conter apenas os andares que ainda não foram visitados;
6. O status parado só ocorre quando a porta é aberta, a mudança para subindo ou
descendo ocorre quando a porta se fecha;
7. Os passageiros só podem embarcar ou desembarcar com o elevador parado e de
portas abertas;
8. Uma rota não pode ser feita com um número superior à capacidade máxima de
pessoas;
9. O Elevador só se movimenta quando a porta se fecha;
10. Assim que selecionado os andares o elevador cria a rota;
11. Se um dos andares selecionados for o andar atual o elevador simplesmente ignora o
andar atual e não o incluir na rota;
12. Os passageiros só podem selecionar o andar de destino da rota com a porta aberta;
13. As exceções de sistema devem ser usadas corretamente (Operações inválidas,
argumentos nulos, etc.). Observação: Sempre que possível, utilize as exceções de
sistema já definidas, ex: NullReferenceException, ArgumentException,
InvalidCastException, etc. ; 
