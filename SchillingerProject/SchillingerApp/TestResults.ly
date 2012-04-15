% Made by the SchillingerApplication of Vassar College 
\version "2.7.38"
\include "english.ly"



\clef treble

trackAchannelA = \relative c'' {

\tempo 4 = 120
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


trackA = << 
   \context Voice = channelA \trackAchannelA 
>> 


trackBchannelA = {

\tempo 4 = 120
\time 3/4
\key f \minor

s1*8*3/4
c''4 c''4 c''4 |
cs''8 cs''8 as'8 gs'8 f'8 g'8 |
gs'8 as'8 as'8 c''8 c''8 c''8 |
as'8 as'8 gs'8 gs'8 g'8 gs'8 |
gs'8 c''4 cs''4 cs''8 ~ |
cs''8 f''8 f''8 f''8 f''8 cs''8 |
cs''8 c''8 c''8 c''8 c''8 c''8 |
c''8 c''8 c''8 c''8 c''8 c''8 |
c''8 
}



trackB = << 
  \context Voice = channelA\trackBchannelA 
>>


trackCchannelA = {

\tempo 4 = 120
\time 3/4
\key f \minor

s1*8*3/4
c''4 c''4 c''4 |
cs''8 cs''8 as'8 gs'8 f'8 g'8 |
gs'8 as'8 as'8 c''8 c''8 c''8 |
as'8 as'8 gs'8 gs'8 g'8 gs'8 |
gs'8 c''8 c''8 cs''8 ds''8 cs''8 |
f''8 f''8 f''8 f''8 f''8 cs''8 |
cs''8 c''8 c''8 c''8 c''8 c''8 |
c''8 c''4 c''4 c''8 ~ |
c''8 
}



trackC = << 
  \context Voice = channelA\trackCchannelA 
>>


trackDchannelA = {

\tempo 4 = 120
\time 3/4
\key f \minor

s1*8*3/4
c''8 c''8 c''8 as'8 c''8 c''8 |
cs''8 cs''8 as'8 gs'8 f'8 g'8 |
gs'8 as'8 as'8 c''8 c''8 c''8 |
as'8 as'8 gs'8 gs'8 g'8 gs'8 |
gs'8 c''8 c''8 cs''8 ds''8 cs''8 |
f''4 f''4 f''4 |
cs''4 c''4 c''4 |
c''8 c''8 c''8 c''8 c''8 c''8 |
c''8 
}



trackD = << 
  \context Voice = channelA\trackDchannelA 
>>


trackEchannelA = {

\tempo 4 = 120
\time 3/4
\key f \minor

s1*8*3/4
c''8 c''8 c''8 as'8 c''8 c''8 |
cs''8 cs''8 as'8 gs'8 f'8 g'8 |
gs'8 as'8 as'8 c''8 c''8 c''8 |
as'4 gs'4 g'4 |
gs'8 c''8 c''8 cs''8 ds''8 cs''8 |
f''8 f''8 f''8 f''8 f''8 cs''8 |
cs''8 c''8 c''8 c''8 c''8 c''8 |
c''8 c''4 c''4 c''8 ~ |
c''8 
}



trackE = << 
  \context Voice = channelA\trackEchannelA 
>>


\score {

  <<
  \context Staff=trackA \trackA
  \context Staff=trackB \trackB
  \context Staff=trackC \trackC
  \context Staff=trackD \trackD
  \context Staff=trackE \trackE
  >>

  \layout {}
  \midi {}

}