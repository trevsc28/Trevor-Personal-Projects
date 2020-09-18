open Stdlib

let _  = Random.self_init ()

type term =
  | Constant of string
  | Variable of string
  | Function of string * term list

type head = term
type body = term list

type clause = Fact of head | Rule of head * body

type program = clause list

type goal = term list

let rec string_of_f_list f tl =
  let _, s = List.fold_left (fun (first, s) t ->
    let prefix = if first then "" else s ^ ", " in
    false, prefix ^ (f t)) (true,"") tl
  in
  s

let rec string_of_term t =
  match t with
  | Constant c -> c
  | Variable v -> v
  | Function (f,tl) ->
      f ^ "(" ^ (string_of_f_list string_of_term tl) ^ ")"

let string_of_term_list fl =
  string_of_f_list string_of_term fl

let string_of_goal g =
  "?- " ^ (string_of_term_list g)

let string_of_clause c =
  match c with
  | Fact f -> string_of_term f ^ "."
  | Rule (h,b) -> string_of_term h ^ " :- " ^ (string_of_term_list b) ^ "."

let string_of_program p =
  let rec loop p acc =
    match p with
    | [] -> acc
    | [c] -> acc ^ (string_of_clause c)
    | c::t ->  loop t (acc ^ (string_of_clause c) ^ "\n")
  in loop p ""

let var v = Variable v
let const c = Constant c
let func f l = Function (f,l)
let fact f = Fact f
let rule h b = Rule (h,b)


(*PROBLEM 1*)
let rec occurs_check v t =
  match t with
  |  Constant y -> false
  |  Variable y -> if v = var y then true else false
  |  Function (f, lst) -> match lst with
                          | [] -> false
                          | hd::tl -> v = hd || (List.fold_left (fun vl tail -> vl || occurs_check v tail) false tl);;
                                   



(*PROBLEM 2*)
module VarSet = Set.Make(struct type t = term let compare = Stdlib.compare end)
(* API Docs for Set : https://caml.inria.fr/pub/docs/manual-ocaml/libref/Set.S.html *)

let rec var_term t set =
  match t with
  | Constant y -> set
  | Variable y -> let vary = var y in VarSet.union (VarSet.singleton vary) set
  | Function (f, lst) ->  match lst with
                          | [] -> set
                          | hd::tl -> List.fold_left(fun acc head -> var_term head acc) set lst;;
let rec variables_of_term t = 
  let set = VarSet.empty in var_term t set;;


let var_clause t set = 
  match t with
  | Fact y -> variables_of_term y
  | Rule (head, body) -> VarSet.union (variables_of_term head) (List.fold_left (fun acc lst -> var_term lst acc) VarSet.empty body)
let variables_of_clause c =
  let set = VarSet.empty in var_clause c set;;





(*PROBLEM 3*)
module Substitution = Map.Make(struct type t = term let compare = Stdlib.compare end)
(* See API docs for OCaml Map: https://caml.inria.fr/pub/docs/manual-ocaml/libref/Map.S.html *)
let string_of_substitution s =
  "{" ^ (
    Substitution.fold (
      fun v t s ->
        match v with
        | Variable v -> s ^ "; " ^ v ^ " -> " ^ (string_of_term t)
        | Constant _ -> assert false (* substitution maps a variable to a term *)
        | Function _ -> assert false (* substitution maps a variable to a term *)
    ) s ""
  ) ^ "}"

let rem_option x = 
  match x with
  | Some x -> x
  | None -> assert false 

let rec substitute_in_term s t = 
  match t with
    | Variable y -> if (Substitution.find_opt t s) = None then (var y) 
                        else let ret = (Substitution.find_opt t s) in rem_option ret
    | Constant y -> (const y)
    | Function (f, lst) -> match lst with
                          | [] -> Function(f, lst)
                          | hd::tl -> Function (f, [(substitute_in_term s hd)]@(List.map (substitute_in_term s) tl))

let substitute_in_clause s c =
  match c with
  | Fact y -> Fact (substitute_in_term s y)
  | Rule (head, body) -> Rule((substitute_in_term s head), (List.map (substitute_in_term s) body))




(* PROBLEM 4 *)
let counter = ref 0
let fresh () =
  let c = !counter in
  counter := !counter + 1;
  Variable ("_G" ^ string_of_int c)

let freshen c =
  let vars = variables_of_clause c in
  let s = VarSet.fold (fun v s -> Substitution.add v (fresh()) s) vars Substitution.empty in
  substitute_in_clause s c

exception Not_unifiable

let rec tuple_of_lists t1 t2 = 
  match (t1, t2) with
  | ([], []) -> []
  | (hd1::tl1, hd2::tl2) -> (hd1, hd2)::(tuple_of_lists tl1 tl2)
  | _ -> []

let rec unifier x y theta =
  let x = substitute_in_term theta x in
  let y = substitute_in_term theta y in
  match x, y with
  | Variable v, _ when (occurs_check (var v) y) = false ->  let theta = Substitution.map (fun t -> if t = (var v) then y else substitute_in_term theta t) theta in Substitution.add x y theta           
  | _ , Variable v when (occurs_check (var v) x) = false ->  let theta = Substitution.map (fun t -> if t = (var v) then x else substitute_in_term theta t) theta in Substitution.add y x theta
  | Constant a, Constant b when x = y -> theta
  | Variable a, Variable b when x = y -> theta 
  | Function (fx, bodyx), Function (fy, bodyy) when (List.length bodyx) = (List.length bodyy) -> if fx = fy then (List.fold_left (fun a (x, y) -> unifier x y a) theta (tuple_of_lists bodyx bodyy)) else raise (Not_unifiable)
  | _ -> raise (Not_unifiable)

let unify t1 t2 =
  unifier t1 t2 Substitution.empty;;


(* PROBLEM 5 *)

let rec remove_From lst n = 
  match lst with
  | [] -> []
  | hd::tl when hd = n -> tl
  | hd::tl -> hd::(remove_From tl n)


  let rec rec_query program goal resolvent = 
    match resolvent with (* Do we even have a goal in the resolvent? *)
    | [] -> goal (* no *)
    | _ ->  (* yes *)

          let r = List.length resolvent in 
          let n = Random.int r in 
          let randomGoal = List.nth resolvent n in (* we have our random goal  a*) 

         (* Split the list of rules into tuple = (list of Unifies, list of NOT Unifies) *)
          let tuple = List.partition (fun x ->
                match x with
                | Fact head -> (try let _ = unify randomGoal head in true with Not_unifiable -> false)
                | Rule (head, body) -> (try let _ = unify randomGoal head in true with Not_unifiable -> false)) program in

          match tuple with
          | ([], _) -> []  (* nothing unifies with A *)
          | (lst, _) ->  (* something unifies with A*)
                
                let r = List.length lst in 
                let n = Random.int r in 
                let rule = List.nth lst n in (* we have our random rule *)
                let randomRule = freshen rule in

                match randomRule with 
                | Fact head ->  let theta = unify randomGoal head in 
                                let rs = remove_From resolvent randomGoal in
                                rec_query (program) (List.map (substitute_in_term theta) goal) (List.map (substitute_in_term theta) (rs))

                | Rule (head, body) -> let theta = unify randomGoal head in 
                                       let rs = remove_From resolvent randomGoal in
                                       rec_query (program) (List.map (substitute_in_term theta) goal) (List.map (substitute_in_term theta) (body@rs))

let rec nondet_query program goal =
  let answer = rec_query program goal goal in if answer = [] then nondet_query program goal else answer





