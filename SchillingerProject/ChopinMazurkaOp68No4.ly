\version "2.12.1"
\include "english.ly"

\score {
  
  \relative c'' {
    \time 3/4
    \key f \minor
    
    c4 ~ c8 df df c |
    b8 as b4 g' |
    bf,4 ~ bf8 c c bf |
    a8 gs a4 f' |
    af,4 ~ af8 bf bf af |
    g8 fs g4 df' |
    c4 ~ c8 bf8 af g |
    bf8 af f2
    
    
    
  }
  
  \midi 
  {
    %tempoWholesPerMinute = #(ly:make-moment 120 4)
  }
  
  \layout {
    
  }
}