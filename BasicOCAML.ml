exception IncorrectRange

(*PROBLEM 1 - Write a function cond_dup : 'a list -> ('a -> bool) -> 'a list that takes in a list and preciate and duplicates all elements which satisfy the condition expressed in the predicate *)
let rec cond_dup l f =
	match l with 
	| [] -> []
	| h::t -> if f(h) then h::h:: cond_dup t f else h:: cond_dup t f;;

(*PROBLEM 2 - Write a function n_times : ('a -> 'a) * int * 'a -> 'a such that n_times (f,n,v) applies f to v n times.*)
let rec n_times (f, n, v) = 
	if n <= 0 then v 
	else n_times(f, n-1, f(v));;

(*PROBLEM 3 - Write a function range : int -> int -> int list such that range num1 num2 returns an ordered list of all integers from num1 to num2 inclusive*)
let rec range num1 num2 =
 if num2 < num1 then raise (IncorrectRange)
 else if num1 == num2 then num1::[]
 else num1::(range (num1+1) num2);;

(*PROBLEM 4 - Write a function zipwith : ('a -> 'b -> 'c) -> 'a list -> 'b list -> 'c list such that zipwith f l1 l2 generates a list whose ith element is obtained by applying f to the ith element of l1 and the ith element of l2 *)
 let rec zipwith f l1 l2 = 
 	match (l1, l2) with
 	| ([], _) -> []
 	| (_, []) -> []
 	| (h1::t1, h2::t2) -> f h1 h2 :: zipwith f t1 t2;;

(*PROBLEM 5 - Write a function remove_stutter : 'a list -> 'a list that removes stuttering from the original list*)
let rec rec_remove l =
	match l with
	| [] -> []
	| h::[] -> h::[]
    | h1::h2::tl -> if h1 = h2 then rec_remove (h2::tl) else h1::rec_remove (h2::tl);;
let remove_stutter l = 
	rec_remove l;;

(*PROBLEM 6 - Write a function flatten : 'a list list -> 'a list that flattens a list.*)
let rec flatten l =
	match l with
	| [] -> []
	| h::t -> h @ flatten t;;

(*PROBLEM 7 - implement a tail recursive function fib_tailrec that uses this idea and computes the nth fibonacci number in linear time. fib_tailrec : int -> int*)
let rec fib n prev curr =
  if n = 1 then curr 
  else fib (n-1) (curr) (prev+curr);;  
let fib_tailrec n = fib n 0 1;;