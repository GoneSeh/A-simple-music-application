import React from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import { Pagination, EffectCoverflow } from "swiper/modules";
import "swiper/css";
import "swiper/css/pagination";
import "swiper/css/effect-coverflow";
import "../styles/Slider.css";

// Import images
import midnightMoods from '../images/MidnightMoods.jpg';
import partyStarters from '../images/partystarters.jpg';
import relaxingTones from '../images/RelaxingTones.jpg';
import smoothJazz from '../images/Jazz.jpg';
import upliftingRhythms from '../images/Uplifting.jpg';

const slides = [
  { id: "ChunShiang", title: "Chun Shiang Playlist", image: midnightMoods },
  { id: "LiewHH", title: "LiewHH Playlist", image: partyStarters },
  { id: "Yang", title: "Yang Playlist", image: relaxingTones },
  { id: "Sister", title: "Celly \u2764\uFE0F \u2764\uFE0F Playlist", image: smoothJazz },
  { id: "Bigboss", title: "Bigboss Playlist", image: upliftingRhythms },
];

const FeaturedSlider = ({ onListenNow }) => {
  return (
    <div className="slider-container">
      <h1>Family Playlist</h1>
      <Swiper
        effect="coverflow"
        grabCursor={true}
        centeredSlides={true}
        loop={true}
        speed={600}
        slidesPerView="auto"
        coverflowEffect={{
          rotate: 10,
          stretch: 120,
          depth: 200,
          modifier: 1,
          slideShadows: false,
        }}
        pagination={{ el: ".swiper-pagination" }}
        modules={[EffectCoverflow, Pagination]}
        className="swiper"
      >
        {slides.map((slide) => (
          <SwiperSlide key={slide.id} className="swiper-slide">
            <img src={slide.image} alt={slide.title} />
            <div className="slide-overlay">
              <h2>{slide.title}</h2>
              <button onClick={() => onListenNow(slide.id)}>
                Listen Now <i className="fa-solid fa-circle-play"></i>
              </button>
            </div>
          </SwiperSlide>
        ))}
        <div className="swiper-pagination"></div>
      </Swiper>
    </div>
  );
};

export default FeaturedSlider;
