import styled from 'styled-components';
import { device, pointColor } from '../../styles/variables';

const NavbarMain = styled.div`
  height: 50%;
  display: flex;
  @media ${device.TabletPortrait} {
    height: 100%;
  }
`;
const LeftMenu = styled.div`
  opacity: 0;
  font-size: 30px;
  display: flex;
  align-items: center;

  color: white;
  @media ${device.TabletPortrait} {
    opacity: 1;
  }
  width: 30%;
  #drawer {
    margin-left: 0.8em;
  }
`;
const CenterMenu = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  img {
    margin-top: 10px;
    width: 200px;
  }
  @media ${device.TabletPortrait} {
    img {
      margin-top: 5px;
      width: 140px;
    }
  }
  width: 50%;
`;
const RightMenu = styled.div`
  font-size: 17px;
  display: flex;
  align-items: center;
  justify-content: right;
  font-size: 20px;
  opacity: 1;
  color: white;
  @media ${device.TabletPortrait} {
    opacity: 0;
  }
  width: 30%;
  #name {
    margin-right: 20px;
  }
`;
const NavbarSubBlock = styled.div`
  display: flex;
  justify-content: center;
  opacity: 0;
  @media ${device.TabletPortrait} {
    display: none;
  }
  height: 40%;
`;
const SubMenu = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  ul li {
    color: white;
    margin: 0 20px;
    text-align: center;
    float: left;
  }
`;
const NavbarWrapper = styled.div`
  &:hover ${NavbarSubBlock} {
    opacity: 1;
    transition: 0.5s;
  }
  height: 12vh;
  @media ${device.TabletPortrait} {
    height: 10vh;
  }
`;

const DrawerListRow = styled.div`
  font-size: 20px;
  text-align: center;
  padding: 10px;
  .name {
    font-weight: bold;
  }
  .color {
    &:hover {
      color: ${pointColor};
      font-weight: bold;
    }
  }
`;

export {
  NavbarWrapper,
  NavbarMain,
  CenterMenu,
  LeftMenu,
  RightMenu,
  NavbarSubBlock,
  SubMenu,
  DrawerListRow,
};
