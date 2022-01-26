import React from 'react';
import styled from 'styled-components';
import StarChosungScreen from '../../../components/room/CommonComponents/MainItems/Game/StarChosungScreen';
// 포지션작업
const BackgroundDiv = styled.div`
  width: 100%;
  height: 100%;
  background-color: #e2d8ff;
`;

export default function StarConsonantGame() {
  return (
    <BackgroundDiv>
      <StarChosungScreen></StarChosungScreen>
    </BackgroundDiv>
  );
}
