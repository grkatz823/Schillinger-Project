% Made by the SchillingerApplication of Vassar College
\include "english.ly"


trackAchannelA =  {
\time 2/4
\tempo 4 = 120
\clef treble
\key g \major
g'2. a'16 e'16 g'16 b'16  |


}


trackA = <<
  \context Voice = channelA \trackAchannelA
>>


\score {
  <<
    \context Staff=trackA \trackA
  >>
}

