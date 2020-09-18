/* PROBLEM 1 */
range(S,E,M) :- M >= S, E >= M.

?- range(1,2,2).
?- not(range(1,2,3)).


/* PROBLEM 2 */
reverseL([], []).
reverseL([H | T], RevX) :- reverseL(T, Z), append(Z, [H], RevX).

?- reverseL([],X).
?- reverseL([1,2,3],X).
?- reverseL([a,b,c],X).


/* PROBLEM 3 */
memberL( _ , []) :- false.
memberL(H, [H | _ ]).
memberL(X, [ H | T ]) :- memberL(X, T).

?- not(memberL(1, [])).
?- memberL(1,[1,2,3]).
?- not(memberL(4,[1,2,3])).
?- memberL(X, [1,2,3]).


/* PROBLEM 4 */
zip([], _ , []).
zip( _ , [], []).
zip([H1 | T1], [H2 | T2], XYs) :- zip(T1, T2, X), append([H1-H2], X, XYs).

?- zip([1,2],[a,b],Z).
?- zip([a,b,c,d], [1,X,y], Z).
?- zip([a,b,c],[1,X,y,z], Z).
?- length(A,2), length(B,2), zip(A, B, [1-a, 2-b]).


/* PROBLEM 5 */
insert( _ , [], []).
insert(H, [], [H]) :- !.
insert(X, [H | T], [X, H | T] ) :- X =< H, !.
insert(X, [H | T], [H | L1]) :- insert(X, T, L1), X >= H.

?- insert(3, [2,4,5], L).
?- insert(3, [1,2,3], L).
?- not(insert(3, [1,2,4], [1,2,3])).
?- insert(3, L, [2,3,4,5]).
?- insert(9, L, [1,3,6,9]).
?- insert(3, L, [1,3,3,5]).


/* PROBLEM 6 */
remove_duplicates(L1, L2) :- rem3(L1, L2, []).
rem3([], ACC, ACC).
rem3([H1 | T1], L, ACC) :-  not(member(H1, ACC)), append(ACC, [H1], Z), rem3(T1, L, Z).
rem3([H1 | T1], L, ACC) :- member(H1, ACC), rem3(T1, L, ACC).

?- remove_duplicates([1,2,3,4,2,3],X).
?- remove_duplicates([1,4,5,4,2,7,5,1,3],X).
?- remove_duplicates([], X).


/* PROBLEM 7 */
intersectionL(L1, L2, RET) :- sort(L1, ZL1), sort(L2, ZL2), intersect4(ZL1, ZL2, RET, []).
intersect4([], _, ACC, ACC).
intersect4(_, [], ACC, ACC).
intersect4([H1 | T1], [H2 | T2], RET, ACC) :- H1 > H2, intersect4([H1 | T1], T2, RET, ACC).
intersect4([H1 | T1], [H2 | T2], RET, ACC) :- H1 < H2, intersect4(T1, [H2 | T2], RET, ACC).
intersect4([H1 | T1], [H2 | T2], RET, ACC) :- H1 = H2, append(ACC, [H1], G), intersect4(T1, T2, RET, G).

?- intersectionL([1,2,3,4],[1,3,5,6],[1,3]).
?- intersectionL([1,2,3,4],[1,3,5,6],X).
?- intersectionL([1,2,3],[4,3],[3]).


/* PROBLEM 8 */
prefix(P,L) :- append(P,_,L).
suffix(S,L) :- append(_,S,L).

partition([], _, _).
partition([H],[H], []).
partition(List, P, S) :-  
    length(List, N), M is div(N, 2),
    prefix(P, List), length(P, Num1), Num1 is M,
    suffix(S, List), length(S, Num2), Num2 is N-M, partition([], P, S).

?- partition([a],[a],[]).
?- partition([1,2,3],[1],[2,3]).
?- partition([a,b,c,d],X,Y).


/* PROBLEM 9 */
merge(X,Y,RET) :- m4(X,Y,RET,[]).
m4([],[],G,G) :- !.
m4([H1 | T1], [H2 | T2], RET, Merge) :- H1 > H2, append(Merge, [H2], Z), m4([H1|T1], T2, RET, Z).
m4([H1 | T1], [H2 | T2], RET, Merge) :- H2 > H1, append(Merge, [H1], Z), m4(T1, [H2|T2], RET, Z).
m4([H1 | T1], [H2 | T2], RET, Merge) :- H1 = H2, append(Merge, [H1, H2], Z), m4(T1, T2, RET, Z).
m4([], L, RET, Merge) :- append(Merge, L, Z), m4([], [], RET, Z).
m4(L, [], RET, Merge) :- append(Merge, L, Z), m4([], [], RET, Z).

?- merge([],[1],[1]).
?- merge([1],[],[1]).
?- merge([1,3,5],[2,4,6],X).


/* PROBLEM 10 */

mergesort([], []) :- !.
mergesort([H], [H]) :- !.
mergesort(List, RET) :- partition(List, Prefix, Suffix), mergesort(Prefix, Sorted_Prefix), mergesort(Suffix, Sorted_Suffix), merge(Sorted_Prefix, Sorted_Suffix, RET).

?- mergesort([3,2,1],X).
?- mergesort([1,2,3],Y).
?- mergesort([],Z).