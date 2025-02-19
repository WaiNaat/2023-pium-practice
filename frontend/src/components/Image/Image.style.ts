import { keyframes, styled } from 'styled-components';

export interface StyledImageProps {
  type: 'circle' | 'square';
  size: string;
}

const wave = (size: string) => keyframes`
  0% {
    background-position: -${size} 0;
  }

  100% {
    background-position: ${size} 0;
  }
`;

export const StyledImage = styled.img<StyledImageProps>`
  display: inline-flex;

  width: ${({ size }) => size};
  height: ${({ size }) => size};

  object-fit: cover;
  background: linear-gradient(
    to right,
    ${({ theme }) => theme.color.primary}33 7%,
    ${({ theme }) => theme.color.primary}4C 17%,
    ${({ theme }) => theme.color.primary}33 37%
  );
  background-size: 77px 77px;
  border-radius: ${({ type }) => (type === 'circle' ? '50%' : 0)};

  animation: ${({ size }) => wave(size)} 2.5s linear infinite;
`;
