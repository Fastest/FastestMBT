# Script for converting Spivey Z into standard Z
# The changes necessary are:
# 0 There needs to be a prelude file fastestprelude.tex introducing
#   symbols like \real and \divides which are not included in Fastest.
#   Note that we wouldn't expect Fastest to understand the semantics of reals,
#   all we are doing is making sure it can accept a spec containing them
#
# 1 A paragraph which is just a predicate must be converted to an axiomatic
#   with an empty declaration.
#   This is currently done very crudely and is far from bullet proof
#   Now we are using the fuzz lisp output (see below) a better way would be to
#   look for PRED tokens in the lisp. However it probably doesn't matter as there
#   only seems to be one in the whole spec
#
# 2 Underlined relation names need to be replaced. In the tex, a R b with R
#   underlined is represented as a \inrel{R} b.
#   The meaning of \inrel can be defined in the prelude file but you can't
#   define an operator with structure \_ \inrel \_ \_ : so I need to insert
#   something between the last two arguments. So I preprocess a \inrel{R} b into
#   a \inrel{R} \lerni b and define \_ \inrel \_ \lerni \_ in the prelude
#
# 3 The rules for making tokens are incredibly obscure in standard Z and
#   "<?" is not lexed as a name - you have to make it into "{<}?"
#
# 4 Standard Z distinguishes unary minus and binary minus. With a stroke of genius
#   unary minus has the Unicode character U+002D (ascii minus) but the latex \negate, while
#   binary minus has the Unicode character U+2212 but the latex - (ascii minus)
#   In Spivey Z they are both - in the latex
#   (and in Z Word Tools typing '-' gives you U+2212)
#   Again, the conversion here is done very crudely and is not bullet proof
#   and it would be better to use the lisp to identify which is which
#
# 5 The schema definition \defs in Spivey is replaced by == in standard Z
#
# 6 There is some rule I don't quite understand requiring the function 'max',
#   in the expression "max0 = max \cup ...." to be replaced with (max \_).
#   There may be similar problems elsewhere that I haven't found yet
#
# 7 The Spivey \LET is used in two contexts: in predicates (let x ==1 @ y=x)
#   and in expressions y = (let x==1 @ x). In standard Z you have to use \exists
#   in the case of predicates. You can still use \LET in the case of expressions
#   but you can also replace it with \mu
#   Fixing this requires a complete parse of the Z.
#   Fortunately the lisp output distinguishes the two cases and produces tokens
#   LETPRED and LETEXPR respectively. So the trick is to generate a dictionary
#   that says for each line what the sequence of let definitions on that line is
#   and use that to do replacements on that line.


import sys
import re
import subprocess

# Convertfile converts a file with the given inName and appends the output to
# the open outFile
def convertFile(inName, outFile):
    inFile = open(inName, 'r')
    zedToAxdef = False
    lineNo = 1
    line = inFile.readline()
    while line:
# Change paragraphs that are just predicates into empty axdefs
# Let's hope the first line has \defs, == or ::= otherwise
        if line[:11] == r'\begin{zed}':
            lineNo = lineNo+1
            line2 = inFile.readline()
            if not re.search(r'(^[~ ]*\[|::=|==|\\defs)',line2):
                outFile.write('\\begin{axdef}\n')
                outFile.write('\\where\n')
                zedToAxdef = True
            else:
                outFile.write(line)
                zedToAxdef = False
            line = line2
        if line[:9] == r'\end{zed}' and zedToAxdef:
            outFile.write('\\end{axdef}\n')
            zedToAxdef = False
        else:
            line = processLine(line, lineNo)
            outFile.write(line)
        lineNo = lineNo+1
        line = inFile.readline()
    inFile.close()


#   ProcessLine applies conversions 2 - 7 to an individual line
def processLine(line, lineNo):
    line = replaceInrel(line)
    line = replaceLessQ(line)
    line = replaceMinus(line)
    line = replaceDefs(line)
    line = fixMaxUse(line)
    line = replaceLets(line, lineNo)
    return line

def replaceInrel(line):
    return re.sub(r'\\inrel\{(.*?)\}',r'\g<0>\\lerni ',line)

def replaceLessQ(line): # add {} and make sure there is a space round it
    return re.sub(r'(<|\\leq)\?',r'~{\1}?~',line)

def replaceMinus(line): # change if it follows a newline or symbol
    return re.sub(r'(^|[+*/:|(]|\\[a-zA-Z]*?)[ ~]*?-',r'\1 \\negate ',line)

def replaceDefs(line):
    return re.sub(r'\\defs',r'==',line)

def fixMaxUse(line):    # max \cup -> (max \_) \cup - not sure why necessary
    return re.sub(r'\bmax\b[ ~]*\\cup',r'(max~\_)~\cup',line)

#   listLets creates a dictionary indexed by line number
#   of the lets on that line in order
#   Each dictionary entry is a list of the let types on the line
def listLets():  
    global lets
    lets = {}
    lispFile = open(lispName, 'r')
    lispLine = lispFile.readline()
    while lispLine:
        letMatch = re.search(r'(LETEXPR|LETPRED) ([0-9]*)', lispLine)
        if letMatch:
            letType = letMatch.group(1)
            letLine = int(letMatch.group(2))
            lets[letLine] = lets.get(letLine,[])+ [letType]
        lispLine = lispFile.readline()
    lispFile.close

def replaceLets(line,lineNo):
    if lineNo in lets:
        pattern = makeLetsPattern(lets[lineNo])
        return re.sub(pattern[0], pattern[1], line)
    else:
        return line

# makeLetsPattern creates a pattern and replacement for each \LET on the line
# based on the list of let types on the line
def makeLetsPattern(letList):  
    pattern = ''
    repl = ''
    group = 2
    for let in letList:
        pattern = pattern + r'(\\LET)(.*?)'
        if let == 'LETEXPR':
            repl = repl + r'\mu'
        else:
            repl = repl + r'\exists'
        repl = repl + r'\g<' + str(group) + r'>'
        group = group + 2
    return [pattern, repl]

# MAIN PROCESS STARTS HERE
# Invoke this script with a list of filenames. The output is in fastestinput.tex
# The files are concatenated into a single .ztt file
# Fuzz is run to create a lisp file
# The lisp file is used to make a dictionary of LETS
# (and could be used for other things like distinguishing unary and binary minus too)
# Then the prelude is read and sent to the output
# Then the ztt file is read, converted and appended to the output

texName = sys.argv[1]
lispName = sys.argv[2]
newTexName = sys.argv[3]

listLets()

newTexFile = open(newTexName, 'w')
    
convertFile(texName, newTexFile)

newTexFile.close()


    
