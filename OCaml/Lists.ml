(******************** Problem 1 ********************)

type 'a element = {
  content : 'a;
  mutable next : 'a element option;
  mutable prev : 'a element option
}

let create () = ref None

let is_empty t = !t = None

let insert_first l c =
  let n = {content = c; next = !l; prev = None} in
  let _ = match !l with
    | Some o -> (o.prev <- Some n)
    | None -> () in
  let _ = (l := Some n) in
  n

let insert_after n c =
  let n' = {content = c; next = n.next; prev = Some n} in
  let _ =  match n.next with
    | Some o -> (o.prev <- (Some n'))
    | None -> () in
  let _ = (n.next <- (Some n')) in
  n'

let remove t elt =
  let prev, next = elt.prev, elt.next in
  let _ = match prev with
    | Some prev -> (prev.next <- next)
    | None -> t := next in
  let _ =  match next with
    | Some next -> (next.prev <- prev)
    | None -> () in
  let _ = (elt.prev <- None) in
  let _ = (elt.next <- None) in
  ()      (* return void *)

let iter t f =
  let rec loop node =
    match node with
      | None -> ()
      | Some el ->
        let next = el.next in
        let _ = f el in
        loop (next)
  in
  loop !t



  let removeOption z = 
   match z with
   |Some x -> x
   |None -> assert false (*z is never none*)

(* LIST TO DLL *)
 let dll_of_list l = 
  let dll = create() in 
  let curr = ref None in
    let rec loop lst = 
      match lst, !dll with 
      | [], _ -> None
      | hd::tl, None -> let _ = (curr := (Some (insert_first dll hd))) in loop tl
      | hd::tl, Some x -> let _ = (curr := (Some (insert_after (removeOption(!curr)) hd))) in loop tl
    in let _ = loop l in dll

(* DLL TO LIST *)
let list_of_dll l =
  let rec lll l = 
  match !l with 
  | None -> []
  | Some x -> x.content::(lll (ref x.next))
in lll l

(*LENGTH FUNCTION*)
let length l =
 let len = ref 0 in 
  let _ = iter l (fun x -> (len := !len + 1)) in
  !len;;

(*DUPLICATE FUNCTION*)
let duplicate l =
    let _ = iter l (fun x -> let _ = (insert_after x x.content) in ()) in l

(*REVERSE FUNCTION*)  
let reverse l = 
   iter l (fun x -> remove l x; insert_first l x.content); l

(******************** Problem 2 ********************)

module type Serializable = sig
  type t
  type content

  val string_of_t : t -> string

  val fold : ('a -> content -> 'a) -> 'a -> t -> 'a
end

module SerializableList (C : Serializable) = struct
  type t = C.t list
  type content = C.t

  let string_of_t l =
    let rec loop acc l = match l with
      | [] -> acc
      | [x] -> acc ^ (C.string_of_t x)
      | x::xs -> loop (acc ^ (C.string_of_t x) ^ ";") xs
    in
    "[" ^ (loop "" l) ^ "]"

  let fold f accum l =
    List.fold_left (C.fold f) accum l
end

module SerializableArray (C : Serializable) = struct
  type t = C.t array
  type content = C.t

  let string_of_t l =
  let x = Array.to_list l in 
    let rec loop acc l = match l with
      | [] -> acc
      | [x] -> acc ^ (C.string_of_t x)
      | x::xs -> loop (acc ^ (C.string_of_t x) ^ ";") xs
    in
    "[|" ^ (loop "" x) ^ "|]"

  let  fold f accum l =
    Array.fold_left (C.fold f) accum l
end

module SerializableIntArray = SerializableArray (struct
  type t = int
  type content = int

  let string_of_t x = string_of_int x

  let fold f i res = f i res
end)

module SerializableIntList = SerializableList (struct
  type t = int
  type content = int

  let string_of_t x = string_of_int x

  let fold f i res = f i res
end)

module SerializableIntArrayArray = SerializableArray(SerializableIntArray)

module SerializableIntListArray = SerializableArray(SerializableIntList)

module SerializableIntArrayList = SerializableList(SerializableIntArray)
