\version "2.12.1"

\score {
  
  \relative c'' {
    \time 4/4
    \key c \major
    
    c2 e4 g
    b,4. d8 c2
    a'2 g4 c
    g4 f e2
    
  }
  
  \midi 
  {
    %tempoWholesPerMinute = #(ly:make-moment 120 4)
  }
  
  \layout {
    
  }
}