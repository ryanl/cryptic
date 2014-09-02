#!/usr/bin/python

rules = [
  "X to replace Y in Z = replace(Z,X,Y)",
  "X's last = substring(X,-1)",
  "last of X = substring(X,-1)"
]

positions =  {1:"first", 2:"second", 3:"third", 4:"fourth", 
              5:"fifth",6:"sixth", 7:"seventh", 8:"eighth",
              9:"ninth", 10:"tenth"}

for (n, name) in positions.items():
  rules.append("{} of X = substring(X,{})".format(name, n))
  rules.append("X's {} = substring(X,{})".format(name, n))

anagram_indicators = [
  "crambled",
  "mixed up",
  "muddled",
  "changed"
]

# TODO: indicate that anagrams shouldn't be parsed further.
# e.g. "canine is broken" probably shouldn't resolve to "god"
for indicator in anagram_indicators:
  rules.append("{} X = anagram(X)")
  rules.append("X is {} = anagram(X)")
  rules.append("X {} = anagram(X)")

rules += [
  "X holding$ = substring(X)",
  "X from the east = reversal(X)",
  "reversed X = reversal(X)"
]

rules += [
  "politician = r",
  "king = r",
  "club = c",
  "clubs = c",
  "spade = s",
  "spades = s",
  "heart = h",
  "hearts = h",
  "diamond = d",
  "diamonds = d",

  "suit = c",
  "suit = d",
  "suit = h",
  "suit = s"
  "X and Y = X Y",
  "X before Y = X Y",
  "X following Y = Y X",
  "X after Y = Y X",
  "X follows Y = Y X",
  "X in Y = wrap(X,Y)",
  "X contained in Y = wrap(X,Y)",
  "X contains Y = wrap(Y,X)",
  "X wrapped in Y = wrap(X,Y)",
  "X inside Y = wrap(X,Y)"
  "X without Y = without(X,Y)",
  
]

nothing_words = [
  "and"
  "is"
]

for word in nothing_words:
  rules.append("{} = ".format(word))

if __name__ == "__main__":
  print "\n".join(rules)
