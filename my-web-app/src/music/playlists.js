// src/music/playlists.js
import APT from '../music/Rose_Ft_Bruno_Mars_-_APT.mp3';
import Nonsense from '../music/Sabrina Carpenter  Nonsense Official Audio.mp3';
import ThatsNot from '../music/Charlie Puth  Thats Not How This Works Lyrics ft Dan  Shay.mp3';
import norest from '../music/No_Rest_Or_Endless_Rest_-_Lisofv.mp3';
import juliusmarx from '../music/Julius_Marx_-_Vices_-_Julius_Marx.mp3';
import criminal from '../music/Criminal_-_Axl_amp_Arth.mp3';
import inmymind from '../music/In_My_Mind_-_Laminar.mp3';
import songwritez from '../music/Songwriterz_-_Alive.mp3';
import loveyouanymore from '../music/Tom_Orlando_-_Love_You_Anymore_(feat._Seven).mp3';
import dreaming from '../music/dreaming-not.sky_-_dreaming-not.mp3';

import aptPic from '../images/APT.jpg';
import nonsensePic from '../images/nonsense.jpg';
import charliePuthPic from '../images/charlieputh.png';

const playlistData = {
  ChunShiang: [
    {
      title: "No Rest Or Endless Rest",
      artist: "Lisofv",
      src: norest,
      cover: aptPic,
    },
    {
      title: "Vices",
      artist: "Julius Marx",
      src: juliusmarx,
      cover: aptPic,
    },
    {
      title: "Redemption",
      artist: "Besomorph & Coopex",
      src: APT,
      cover: aptPic,
    },
    // Add more songs as needed
  ],
  LiewHH: [
    {
      title: "Nonsense",
      artist: "Sabrina",
      src: Nonsense,
      cover: nonsensePic,
    },
    {
      title: "Criminal",
      artist: "AXL",
      src: criminal,
      cover: nonsensePic,
    },
    {
      title: "In My Mind",
      artist: "Laminar",
      src: inmymind,
      cover: nonsensePic,
    },
    // Add more songs as needed
  ],
  Yang: [
    {
      title: "Thats Not How Its Worked",
      artist: "Charlie Puth",
      src: ThatsNot,
      cover: charliePuthPic,
    },
    {
      title: "Alive",
      artist: "Song Writez",
      src: songwritez,
      cover: charliePuthPic,
    },
    {
      title: "Love You Anymore",
      artist: "Tom Orlando",
      src: loveyouanymore,
      cover: charliePuthPic,
    },

    // Add more songs as needed
  ],
  // Add other playlists similarly
};

export default playlistData;
